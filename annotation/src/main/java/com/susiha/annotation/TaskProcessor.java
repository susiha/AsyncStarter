package com.susiha.annotation;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
@SupportedAnnotationTypes("com.susiha.annotation.TaskAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TaskProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils    = processingEnvironment.getTypeUtils();
        messager     = processingEnvironment.getMessager();
        filer        = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if(Utils.isEmpty(set)){
            return false;
        }

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TaskAnnotation.class);

        if(Utils.isEmpty(elements)){
            return false;
        }


        TypeElement taskElement  = elementUtils.getTypeElement("com.susiha.library.Task");
        TypeMirror taskMirrir =taskElement.asType();


        //返回的类型
        TypeName taskReturns = ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get("com.susiha.library", "Task"));

        messager.printMessage(Diagnostic.Kind.NOTE,"returns : "+taskReturns.toString());


        MethodSpec.Builder methodSpecBuild =MethodSpec.methodBuilder("getTaskList")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .returns(taskReturns);

        //第一步创建一个List<Task> 对象
        //格式：List<Task> list = new ArrayList<>();
        methodSpecBuild.addStatement("$T list = new $T<>()",taskReturns,ClassName.get(ArrayList.class));

        for (Element element : elements) {
            //如果当前解析的类是Task的子类 打印信息
            if(typeUtils.isSubtype(element.asType(),taskMirrir)){
                messager.printMessage(Diagnostic.Kind.NOTE,"element的类型是 : "+element.asType().toString());
                methodSpecBuild.addStatement("list.add(new $T($S))",element.asType(),element.getAnnotation(TaskAnnotation.class).value());
            }

        }

        methodSpecBuild.addStatement("return list");

        try {
            JavaFile.builder("com.susiha",
                    TypeSpec.classBuilder("TaskUtils")
                            . addModifiers(Modifier.PUBLIC)
                            .addMethod(methodSpecBuild.build())
                            .build()

            ).build()
                    .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }
}
