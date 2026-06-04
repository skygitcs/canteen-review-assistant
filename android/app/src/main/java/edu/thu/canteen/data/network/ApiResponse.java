package edu.thu.canteen.data.network;

public class ApiResponse<T> {
    public int code;
    public String message;
    public T data;

    public boolean isSuccess() {
        return code == 0;
    }
}
