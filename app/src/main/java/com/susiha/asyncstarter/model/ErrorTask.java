package com.susiha.asyncstarter.model;

import com.susiha.annotation.TaskAnnotation;

/**
 * 这种定义了TaskAnnotation注解的类不是Task的子类,不会被运行
 */
@TaskAnnotation("ErrorTask")
public class ErrorTask {
}
