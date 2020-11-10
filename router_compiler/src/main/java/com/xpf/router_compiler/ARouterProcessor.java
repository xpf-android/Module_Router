package com.xpf.router_compiler;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xpf.router_annotation.ARouter;
import com.xpf.router_compiler.utils.Constants;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


// AutoService则是固定的写法，加个注解即可
// 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
// 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
@AutoService(Processor.class)
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})// 允许/支持的注解类型全类名，让注解处理器处理（新增annotation module）
@SupportedSourceVersion(SourceVersion.RELEASE_7)// 指定JDK编译版本
@SupportedOptions("content")// 注解处理器接收的参数
public class ARouterProcessor extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements elementUtils;

    // type(类信息)工具类，包含用于操作TypeMirror的工具方法
    private Types typeUtils;

    // 用来报告错误，警告和其他提示信息
    private Messager messager;

    // 文件生成器 类/资源，Filter用来创建新的源文件，class文件以及辅助文件
    private Filer filer;

    // 该方法主要用于一些初始化的操作，通过该方法的参数ProcessingEnvironment可以获取一些列有用的工具类
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        // 父类受保护属性，可以直接拿来使用。
        // 其实就是init方法的参数ProcessingEnvironment
        // processingEnv.getMessager(); //参考源码64行
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();

        // 通过ProcessingEnvironment去获取build.gradle(app module)传过来的参数
        String content = processingEnv.getOptions().get("content");
        // 有坑：Diagnostic.Kind.ERROR，异常会自动结束，不像安卓中Log.e那么好使
        messager.printMessage(Diagnostic.Kind.NOTE, content);


    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (set.isEmpty()) return false;
        //获取所有被@ARouter注解的类节点
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elements) {
            //类节点上一个节点：包节点
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            //获取简单类名
            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "被@ARouter注解的类：" + className);
            //最终生成的类文件名
            String finalClassName = className + "$$ARouter";

            ARouter aRouter = element.getAnnotation(ARouter.class);

            //构建方法体
            //public static Class<?> findTargetClass() {}
            MethodSpec methodSpec = MethodSpec.methodBuilder("findTargetClass")//方法名
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//方法权限public static
                    .returns(Class.class)//返回值类型
                    .addParameter(String.class, "path")//添加方法参数(String path)
                    // return path.equals("/app/MainActivity") ? MainActivity.class : null;
                    .addStatement("return path.equals($S) ? $T.class : null",
                            aRouter.path(),//$S 参数值, 字符串 /app/MainActivity
                            ClassName.get((TypeElement) element))//$T 类名
                    .build();

            //构建类
            TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(methodSpec)
                    .build();

            //生成文件
            JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                    .build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
