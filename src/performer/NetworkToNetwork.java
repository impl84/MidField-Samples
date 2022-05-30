
package performer;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.StreamInfo;

import util.ConsolePrinter;
import util.ConsoleReader;
import util.SimpleViewer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: NetworkToNetwork
 *
 * Date Modified: 2022.06.09
 *
 */
public class NetworkToNetwork
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SENDER_ADDR = "172.16.126.156";
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //      
    public static void main(String[] args)
    {
        // コンソールからの文字入力を扱う ConsoleReader のインスタンスを取得する．
        ConsoleReader reader = ConsoleReader.getInstance();
        
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // MidField System を初期化し，起動する．
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // ストリーム情報リストを送信ホストから取得する．
            StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
            List<StreamInfo>  lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(SENDER_ADDR);
            if (lsStmInf.size() <= 0) {
                throw new IOException("  ※受信可能なストリームがありません．");
            }
            // SegmentIo の入力を受信ストリームとして構成する．
            SegmentIo segIo = new SegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // SegmentIo の出力を送信ストリームとして構成する．
            segIo.configureOutgoingStream(
                segIo.getOutputVideoFormatList().get(0),
                segIo.getOutputAudioFormatList().get(0)
            );
            // トランスポートプロトコルの設定を行う．
            segIo.setTransportProtocol(
                ProtocolType.TCP,       // TCPを利用する．
                ConnectionMode.PASSIVE  // コネクション接続要求を受け入れる．
            );
            // オプションの設定をする．
            segIo.setPreviewer();       // プレビューワ―を利用する．
            segIo.setLiveSource(true);  // ライブソースオプションを有効にする．
            
            // SegmentIo をもとに StreamPerformer を生成する．
            pfmr = StreamPerformer.newInstance(segIo);
            
            // StreamPerformer から VideoCanvas を取得する．
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            
            // StreamPerformer から VideoCanvas を取得し，SimpleViewer に追加する．
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Device to Network", vidCvs);
            });
            // 入出力処理を開始する．
            pfmr.open();
            pfmr.start();
            
            // Enterキーの入力を待つ．
            System.out.print("> Enter キーの入力を待ちます．");
            reader.readLine();
            
            // 入出力処理を終了する．
            pfmr.stop();
            pfmr.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer の処理を終了する．
            if (pfmr != null) {
                pfmr.delete();
            }
            // MidField System を終了する．
            if (mfs != null) {
                mfs.shutdown();
            }
            // ConsoleReader を解放する．
            reader.release();
        }
    }
}