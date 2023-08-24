
package rpc.performer;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.client.MfsRemote;
import com.midfield_system.rpc.api.client.RemoteControlException;
import com.midfield_system.rpc.api.client.StreamPerformer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class NetworkToNetwork
{
    public static void main(String[] args)
    {
        // MidField System のログ出力先をコンソールに設定する．
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsRemote       mfsRmt = null;
        StreamPerformer pfmr   = null;
        
        try {
            // コマンドライン引数からサーバのIPアドレスとポート番号，
            // 及び送信元のIPアドレスを取得する．
            var serverAddr = args[0];
            int serverPort = Integer.parseInt(args[1]);
            var senderAddr = args[2];
            
            // ▼サーバと接続し，遠隔操作を開始する．
            mfsRmt = new MfsRemote(serverAddr, serverPort, err -> System.err.println(err));
            mfsRmt.initializeRemoteControl();
            
            // ▼受信ストリームで入力を構成する．
            // ・送信元が送信しているストリームを選択して設定する．
            var stmInfMgr = mfsRmt.getStreamInfoManager();
            var lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(senderAddr);
            if (lsStmInf.size() <= 0) {
                System.out.println("※受信可能なストリームがありません．");
                return;
            }
            // RcSegmentIo の入力を受信ストリームとして構成する．
            var segIo = mfsRmt.newSegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // ▼送信ストリームで出力を構成する．
            // ・送信フォーマットを選択して設定する．
            // ・TCPを利用し，コネクション接続要求を受け入れる．
            // ・推奨プレビューワ―を利用する．
            var lsVidFmt = segIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("※送信可能なビデオフォーマットがありません．");
                return;
            }
            var lsAudFmt = segIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("※送信可能なオーディオフォーマットがありません．");
                return;
            }
            segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            segIo.setPreferredPreviewer();
            
            // ▼RcStreamPerformer を生成する．
            pfmr = mfsRmt.newStreamPerformer(segIo);
            
            // ▼RcStreamPerformer の入出力処理を開始する．
            pfmr.start();
            
            // ▼RcStreamPerformer の入出力処理を終了する．
            System.out.print("> Enter キーの入力を待ちます．");
            System.in.read();
            
            pfmr.stop();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ▼RcStreamPerformer の全ての処理を終了する．
            if (pfmr != null) {
                pfmr.delete();
            }
            // ▼サーバと切断し，遠隔操作を終了する．
            if (mfsRmt != null) {
                mfsRmt.shutdownRemoteControl();
            }
        }
    }
}
