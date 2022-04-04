package models.validators;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import actions.views.AttendanceView;
import constants.MessageConst;

/**
 * 勤怠インスタンスに設定されている値のバリデーションを行うクラス
 */
public class AttendanceValidator {

    /**
     * 勤怠インスタンスの各項目についてバリデーションを行う
     * @param av 勤怠インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(AttendanceView av) {
        List<String> errors = new ArrayList<String>();

        //出勤のチェック
        String attendError = validateAttend(av.getAttendAt());
        if (!attendError.equals("")) {
            errors.add(attendError);
        }

        //退勤のチェック
        String leaveError = validateLeave(av.getLeaveAt());
        if (!leaveError.equals("")) {
            errors.add(leaveError);
        }

        return errors;
    }

    /**
     * 出勤時間に入力値があるかをチェックし、すでに入力されていればエラーメッセージを返却
     * @param attendAt 出勤時間
     * @return エラーメッセージ
     */
    private static String validateAttend(LocalDateTime attendAt) {
        if (attendAt == null || attendAt.equals("")) {
            return MessageConst.E_NOATTEND.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 退勤時間に入力値があるかをチェックし、すでに入力されていればエラーメッセージを返却
     * @param leaveAt 退勤時間
     * @return エラーメッセージ
     */
    private static String validateLeave(LocalDateTime leaveAt) {
        if (leaveAt == null || leaveAt.equals("")) {
            return MessageConst.E_NOLEAVE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }
}