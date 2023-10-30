package com.futing.diary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ExceptionResponseDTO
{

  private final LocalDateTime timeStamp;
  private final Integer status;
  private final String exceptionMessage;
  private final String path;

}
