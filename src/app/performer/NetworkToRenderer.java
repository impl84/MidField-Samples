
package app.performer;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.midfield.MfsApplication;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.13
 *
 */
public class NetworkToRenderer
{
    public static void main(String[] args)
    {
        StreamPerformer pfmr = null;
        
        try {
            // コマンドライン引数から送信元のIPアドレスを取得する．
            var senderAddr = args[0];
            
            // ▼MidField System のアプリケーションを起動する．
            var mfsApp = MfsApplication.launch();
            
            // ▼受信ストリームで入力を構成する．
            // ・送信元が送信しているストリームを選択して設定する．
            var stmInfMgr = StreamInfoManager.getInstance();
            var lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(senderAddr);
            if (lsStmInf.size() <= 0) {
                System.out.println("※受信可能なストリームがありません．");
                return;
            }
            var segIo = new SegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // ▼推奨レンダラで出力を構成する．
            segIo.configurePreferredRenderer();
            
            // ▼StreamPerformer を生成し，UIへ追加する．
            pfmr = StreamPerformer.newInstance(segIo);
            mfsApp.addStreamPerformer(pfmr);
            
            // ▼StreamPerformer の入出力処理を開始する．
            pfmr.open();
            pfmr.start();
            
            // ▼StreamPerformer の入出力処理を終了する．
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
            
            pfmr.stop();
            pfmr.close();
        }
        catch (MfsException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ▼StreamPerformer の全ての処理を終了する．
            if (pfmr != null) {
                pfmr.delete();
            }
        }
    }
}