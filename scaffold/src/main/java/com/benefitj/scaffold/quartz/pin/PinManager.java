package com.benefitj.scaffold.quartz.pin;

import com.benefitj.core.WrappedMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface PinManager extends Map<String, PinMethod> {


  static PinManager newManager() {
    return new SimplePinManager();
  }


  class SimplePinManager implements PinManager, WrappedMap<String, PinMethod> {

    private final Map<String, PinMethod> methods = new ConcurrentHashMap<>();

    @Override
    public Map<String, PinMethod> source() {
      return methods;
    }
  }

}
