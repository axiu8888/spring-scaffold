package com.benefitj.scaffold.common;

/**
 * 逻辑异常
 */
public class LogicException extends RuntimeException {

  private boolean showMessage = true;

  public LogicException() {
    this(true);
  }

  public LogicException(boolean showMessage) {
    this.showMessage = showMessage;
  }

  public LogicException(String message) {
    this(message, true);
  }

  public LogicException(String message, boolean showMessage) {
    super(message);
    this.showMessage = showMessage;
  }

  public LogicException(String message, Throwable cause, boolean showMessage) {
    super(message, cause);
    this.showMessage = showMessage;
  }

  public LogicException(Throwable cause) {
    this(cause, true);
  }


  public LogicException(Throwable cause, boolean showMessage) {
    super(cause);
    this.showMessage = showMessage;
  }

  public LogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, boolean showMessage) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.showMessage = showMessage;
  }

  public boolean isShowMessage() {
    return showMessage;
  }

  public void setShowMessage(boolean showMessage) {
    this.showMessage = showMessage;
  }

}
