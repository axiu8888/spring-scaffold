package com.benefitj.scaffold.quartz.pin;

import java.lang.reflect.Method;

public class PinMethod {

  private final Method method;

  private final Pin pin;

  public PinMethod(Method method, Pin pin) {
    this.method = method;
    this.pin = pin;
  }

  public Method getMethod() {
    return method;
  }

  public Pin getPin() {
    return pin;
  }

  public String getPinName() {
    return getPin().name();
  }

  public PinParam[] getPinParams() {
    return getPin().params();
  }

  public String getPinDescription() {
    return getPin().description();
  }

}
