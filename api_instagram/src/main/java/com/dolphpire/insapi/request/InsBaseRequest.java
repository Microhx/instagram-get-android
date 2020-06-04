package com.dolphpire.insapi.request;


import com.dolphpire.insapi.manager.IGConfig;
import com.dolphpire.insapi.response.InsBaseResponseData;

public abstract class InsBaseRequest<R extends InsBaseResponseData> {

  protected InsRequestCallBack<R> mInsRequestCallBack;
  private String tag = "";

  protected String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  protected String getRequestUrl() {
    return IGConfig.API_V1 + getActionUrl();
  }

  protected abstract String getActionUrl();

  abstract void execute();

  public void execute(InsRequestCallBack<R> insRequestCallBack) {
    this.mInsRequestCallBack = insRequestCallBack;
    execute();
  }


}
