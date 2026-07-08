package com.example.studentmanagement.response;


//<T> it is java generic why we use this is
//today -ApiResponse<StudentResponse>
//tomorrow-ApiResponse<DepartmentResponse>
//tomorrow-ApiResponse<CourseResponse>
//One class- Unlimited response types.
public class ApiResponse <T>{
    private boolean success;
    private String message;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ApiResponse(boolean success,
                       String message,
                       T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ApiResponse() {
    }
}
