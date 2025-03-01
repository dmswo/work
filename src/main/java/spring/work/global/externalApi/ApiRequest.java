package spring.work.global.externalApi;

public interface ApiRequest <Request, Response>{
    Response get(Request request);
    Response post(Request request);
    Response patch(Request request);
}
