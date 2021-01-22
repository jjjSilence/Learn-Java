package com.example.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes(value = {"com.example.processor.InterfaceAnnotation"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class InterfaceProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("ooooooooooooooooo");
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "进入到InterfaceProcessor中了~~~");
        // 将带有InterfaceProcessor的类给找出来
        Set<? extends Element> clazz = roundEnv.getElementsAnnotatedWith(InterfaceAnnotation.class);
        clazz.forEach(item -> {
            // 生成一个 I + 类名的接口类
            String className = item.getSimpleName().toString();
            className = "I" + className.substring(0, 1) + className.substring(1);
            TypeSpec typeSpec = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC).build();

            try {
                // 生成java文件
                JavaFile.builder("com.example.processor", typeSpec).build().writeTo(new File("./src/main/java/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}

