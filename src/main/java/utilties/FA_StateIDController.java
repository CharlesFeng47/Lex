package utilties;

/**
 * Created by cuihua on 2017/10/26.
 */
public class FA_StateIDController {

    /**
     * 代表从此Controller中取到的当前可使用的ID
     */
    private static int nowID;

    private FA_StateIDController() {
        FA_StateIDController.nowID = 0;
    }

    public static int getID() {
        return FA_StateIDController.nowID;
    }

    public static void setID(int nowID) {
        FA_StateIDController.nowID = nowID;
    }
}
