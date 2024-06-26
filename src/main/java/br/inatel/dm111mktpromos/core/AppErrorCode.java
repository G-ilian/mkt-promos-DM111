package br.inatel.dm111mktpromos.core;


import org.springframework.http.HttpStatus;

public enum AppErrorCode {

    PRODUCT_NOT_FOUND("entity.product.not-found", "The Product does not exist.", HttpStatus.NOT_FOUND),
    PRODUCTS_NOT_FOUND("entity.products.not-found", "The Product(s) do(es) not exist.", HttpStatus.NOT_FOUND),
    SUPERMARKET_LIST_NOT_FOUND("entity.supermarket-list.not-found", "The list does not exist.", HttpStatus.NOT_FOUND),

    PRODUCTS_QUERY_ERROR("query.product.error", "The product query is not working. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),
    SUPERMARKET_LIST_QUERY_ERROR("query.supermarket-list.error", "The supermarket list query is not working. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),
    USERS_QUERY_ERROR("query.user.error", "The user query is not working. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),

    PASSWORD_ENCRYPTION_ERROR("encrypt.user.error", "The user encryption did not work!", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_CONFLICT_EMAIL("entity.user.conflict", "There is a conflicted user.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("entity.uer.not-found", "The user does not exist.", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("auth.user.invalid-credentials", "The provided credentials are invalid.", HttpStatus.UNAUTHORIZED),
    PRODUCTS_OPERATION_NOT_ALLOWED("entity.products.operation-not-allowed", "The operation is not allowed.", HttpStatus.FORBIDDEN),
    SUPERMARKET_LIST_OPERATION_NOT_ALLOWED("entity.supermarket-list.operation-not-allowed", "The operation is not allowed.", HttpStatus.FORBIDDEN),
    PROMO_INVALID_DATE("entity.promo.invalid-date","The promo isn't more available." ,HttpStatus.NOT_FOUND ),
    PROMO_QUERY_ERROR("query.promo.error", "The promotion query is not working. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR),
    PROMO_OPERATION_NOT_ALLOWED("entity.promo.operation-not-allowed","The operation isn't allowed!",HttpStatus.FORBIDDEN)
    ,USER_UNAUTHORIZED_ACCESS("entity.promo.unauthorized-operarion","The user isn't authorized for this operation",HttpStatus.FORBIDDEN);

    private String code;
    private String message;
    private HttpStatus status;

    AppErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
