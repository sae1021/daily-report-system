package services;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import actions.views.AttendanceConverter;
import actions.views.AttendanceView;
import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Attendance;
import models.validators.AttendanceValidator;

/**
 * 勤怠テーブルの操作に関わる処理を行うクラス
 */
public class AttendanceService extends ServiceBase {

    /**
     * 指定した従業員が作成した勤怠データを、指定されたページ数の一覧画面に表示する分取得しAttendanceViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<AttendanceView> getMinePerPage(EmployeeView employee, int page) {

        List<Attendance> attendances = em.createNamedQuery(JpaConst.Q_ATT_GET_ALL_MINE, Attendance.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return AttendanceConverter.toViewList(attendances);
    }

    /**
     * 指定した従業員が作成した勤怠データの件数を取得し、返却する
     * @param employee
     * @return 勤怠データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_ATT_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示する勤怠データを取得し、AttendanceViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<AttendanceView> getAllPerPage(int page) {

        List<Attendance> attendances = em.createNamedQuery(JpaConst.Q_ATT_GET_ALL, Attendance.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        
        return AttendanceConverter.toViewList(attendances);
    }

    /**
     * 勤怠テーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long attendances_count = (long) em.createNamedQuery(JpaConst.Q_ATT_COUNT, Long.class)
                .getSingleResult();
        return attendances_count;
    }

    /**
     * idを条件に取得したデータをAttendanceViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public AttendanceView findOne(int id) {
        return AttendanceConverter.toView(findOneInternal(id));
    }

    /**
     * 開始時刻のバリデーションチェックを行う
     * @param av 勤怠の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> validateStartTime(AttendanceView av) {
    	List<String> errors = new ArrayList<String>();
    	String error = AttendanceValidator.validateStartTime(av);
        if (!error.equals("")) {
            errors.add(error);
        }
    	return errors;
    }

    /**
     * 終了時刻のバリデーションチェックを行う
     * @param av 勤怠の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> validateEndTime(AttendanceView av) {
    	List<String> errors = new ArrayList<String>();
    	String error = AttendanceValidator.validateEndTime(av);
        if (!error.equals("")) {
            errors.add(error);
        }
    	return errors;
    }
    
    /**
     * 指定した従業員の最新の勤怠情報を取得する
     * @param ev 従業員情報
     * @return 取得データのインスタンス
     */
    public AttendanceView findPrev(EmployeeView ev) {
        return AttendanceConverter.toView(findPrevOneInternal(ev));
    }

    /**
     * 従業員を条件にデータを1件取得する
     * @param employee
     * @return 取得データのインスタンス
     */
    private Attendance findPrevOneInternal(EmployeeView employee) {
        // 従業員を条件に前回の登録データを1件取得する
    	List<Attendance> a = em.createNamedQuery(JpaConst.Q_ATT_GET_ALL_MINE, Attendance.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getResultList();
		if (a == null || a.size() == 0) {
			return new Attendance(null, EmployeeConverter.toModel(employee), null, null, null);
		}
        return a.get(0);
    }
    
    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Attendance findOneInternal(int id) {
    	return em.find(Attendance.class, id);
    }

    /**
     * 勤怠時刻データを1件作成し、勤怠テーブルに登録する
     * @param av 勤怠の登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(AttendanceView av) {

        //バリデーションを行う
        List<String> errors = validateStartTime(av);
        
        if (errors.size() == 0) {
        	//　出勤時刻に現在時刻に設定
            LocalDate day = LocalDate.now();
            LocalDateTime time = LocalDateTime.now();
            AttendanceView avNew = new AttendanceView(
                    // パラメータの値をもとに出勤情報のインスタンスを作成する
                            null,
                            av.getEmployee(), // ログインしている従業員を、日報作成者として登録する
                            day,
                            time,
                            null
                            );

            createInternal(avNew);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }
    
    /**
     * 勤怠データを1件登録する
     * @param av 勤怠データ
     * @throws ParseException 
     */
    private void createInternal(AttendanceView av) {

        em.getTransaction().begin();
        em.persist(AttendanceConverter.toModel(av));
        em.getTransaction().commit();

    }

    /**
     * 画面から入力された日報の登録内容を元に、日報データを更新する
     * @param rv 日報の更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(AttendanceView av) {

        //バリデーションを行う
        List<String> errors = validateEndTime(av);

        if (errors.size() == 0) {
        	//　退勤時刻に現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            av.setLeaveAt(ldt);

            updateInternal(av);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }
    
    /**
     * 勤怠データを更新する
     * @param av 勤怠データ
     */
    private void updateInternal(AttendanceView av) {

        em.getTransaction().begin();
        Attendance a = findOneInternal(av.getId());
        AttendanceConverter.copyViewToModel(a, av);
        em.getTransaction().commit();

    }

}