
package trial.performer;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.StreamInfo;

import util.ConsolePrinter;
import util.SimpleViewer;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.08.28
 *
 */
public class NetworkToRenderer
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // コマンドライン引数から送信元のIPアドレスを取得する．
            String senderAddr = args[0];
            
            // ▼MidField System を初期化し，起動する．
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // ▼受信ストリームで入力を構成する．
            // ・送信元が送信しているストリームを選択して設定する．
            StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
            List<StreamInfo>  lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(senderAddr);
            if (lsStmInf.size() <= 0) {
                System.out.println("※受信可能なストリームがありません．");
                return;
            }
            SegmentIo segIo = new SegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // ▼出力をレンダラとして構成する．
            segIo.configureRenderer();
            
            // ▼StreamPerformer を生成し，その内部にある VideoCanvas を取り出す．
            //   その後，VideoCanvas を表示するための SimpleViewer を生成して表示する．
            pfmr = StreamPerformer.newInstance(segIo);
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Network to Renderer", vidCvs);
            });
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
            // ▼MidField System を終了する．
            if (mfs != null) {
                mfs.shutdown();
            }
        }
    }
}