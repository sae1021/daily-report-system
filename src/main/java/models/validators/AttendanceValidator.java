package models.validators;

import actions.views.AttendanceView;
import constants.MessageConst;

/**
 * 勤怠インスタンスに設定されている値のバリデーションを行うクラス
 */
public class AttendanceValidator {

    /**
     * 前回勤怠データに 出勤記録あり かつ 退勤記録なし の場合（未退勤状態）エラーメッセージを返却
     * @param av 前回勤怠データ
     * @return エラーメッセージ
     */
    public static String validateStartTime(AttendanceView av) {
    	String result = "";
    	if (av.getAttendAt() != null && av.getLeaveAt() == null) {
    		result = MessageConst.E_NOLEAVE.getMessage();
    	}
    	// 前回勤怠データ退勤済み状態なのでエラーなし
		return result;
    }
    
    /**
     * 前回勤怠データがない　または　前回勤怠データに 出勤記録なし の場合（未出勤状態）エラーメッセージを返却
     * @param av 前回勤怠データ
     * @return エラーメッセージ
     */
    public static String validateEndTime(AttendanceView av) {
    	String result = "";
    	if (av.getId() == null || av.getLeaveAt() != null) {
    		result = MessageConst.E_NOATTEND.getMessage();
    	}
    	
    	// 前回勤怠データ出勤済み状態なのでエラーなし
		return result;
    }
	
}