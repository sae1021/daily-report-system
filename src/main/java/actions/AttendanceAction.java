package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.AttendanceView;
import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.AttendanceService;

/**
 * 勤怠に関する処理を行うActionクラス
 *
 */
public class AttendanceAction extends ActionBase {

    private AttendanceService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new AttendanceService();

        //メソッドを実行
        invoke();

        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     **/

    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する勤怠データを取得
        int page = getPage();
        List<AttendanceView> attendances = service.getAllPerPage(page);

        //全勤怠データの件数を取得
        long attendancesCount = service.countAll();

        putRequestScope(AttributeConst.ATTENDANCES, attendances); //取得した勤怠データ
        putRequestScope(AttributeConst.ATT_COUNT, attendancesCount); //全ての勤怠データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_ATT_INDEX);
    }

    /**
     * 勤怠管理画面を表示する
     * @throws ServletException
     * @throws IOException
     **/

    public void manage() throws ServletException, IOException {

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        AttendanceView avi = new AttendanceView();
        avi.setAttendanceDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, avi); //日付のみ設定済みの日報インスタンス

        // 勤怠管理画面を表示
        forward(ForwardConst.FW_ATT_MANAGE);
    }

    /**
     * 出勤登録を行う
     * @throws ServletException
     * @throws IOException
     *
     **/
    public void entryIn() throws ServletException, IOException {

        // セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        // 前回の勤怠入力情報を取得
        AttendanceView av = service.findPrev(ev);

        // 勤怠情報登録
        List<String> errors = service.create(av);
        if (errors.size() > 0) {
            // 登録中にエラーがあった場合

            putRequestScope(AttributeConst.ERR, errors);// エラーのリスト

            // 勤怠管理画面を再表示
            forward(ForwardConst.FW_ATT_MANAGE);

        } else {
            //登録中にエラーがなかった場合

            //セッションに登録完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_ATT, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * 退勤登録を行う
     * @throws ServletException
     * @throws IOException
     *
     **/
    public void entryOut() throws ServletException, IOException {

        // セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        // 前回の勤怠入力情報を取得
        AttendanceView av = service.findPrev(ev);

        List<String> errors = service.update(av);
        if (errors.size() > 0) {
            // 登録中にエラーがあった場合

            putRequestScope(AttributeConst.ERR, errors);// エラーのリスト

            // 勤怠管理画面を再表示
            forward(ForwardConst.FW_ATT_MANAGE);

        } else {
            //登録中にエラーがなかった場合

            //セッションに登録完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_ATT, ForwardConst.CMD_INDEX);
        }
    }
}