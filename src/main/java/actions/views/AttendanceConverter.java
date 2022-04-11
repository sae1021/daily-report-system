package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Attendance;

/**
 * 勤怠データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class AttendanceConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param avi AttendanceViewのインスタンス
     * @return Attendanceのインスタンス
     */
    public static Attendance toModel(AttendanceView avi) {
        return new Attendance(
                avi.getId(),
                EmployeeConverter.toModel(avi.getEmployee()),
                avi.getAttendanceDate(),
                avi.getAttendAt(),
                avi.getLeaveAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param a Attendanceのインスタンス
     * @return AttendanceViewのインスタンス
     */
    public static AttendanceView toView(Attendance a) {

        if (a == null) {
            return null;
        }

        return new AttendanceView(
                a.getId(),
                EmployeeConverter.toView(a.getEmployee()),
                a.getAttendanceDate(),
                a.getAttendAt(),
                a.getLeaveAt());
    }
    
    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<AttendanceView> toViewList(List<Attendance> list) {
        List<AttendanceView> avs = new ArrayList<>();

        for (Attendance a : list) {
            avs.add(toView(a));
        }

        return avs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param a DTOモデル(コピー先)
     * @param avi Viewモデル(コピー元)
     */
    public static void copyViewToModel(Attendance a, AttendanceView avi) {
        a.setId(avi.getId());
        a.setEmployee(EmployeeConverter.toModel(avi.getEmployee()));
        a.setAttendanceDate(avi.getAttendanceDate());
        a.setAttendAt(avi.getAttendAt());
        a.setLeaveAt(avi.getLeaveAt());

    }

}