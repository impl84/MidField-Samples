
package rc.performer;

import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.util.Map;

import com.midfield_system.api.rpc.ErrorResponseHandler;
import com.midfield_system.api.util.Log;

import rc.RcDeviceInfo;
import rc.RcDeviceInfoManager;
import rc.RcMfsNode;
import rc.RcSegmentIo;
import rc.RcStreamPerformer;
import util.ConsolePrinter;
import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcDeviceToRenderer
 * 
 * Date Modified: 2022.06.09
 *
 */
public class RcDeviceToRenderer
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_ADDR = "172.16.126.170";
    private static final int    SERVER_PORT = 60202;
    
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
        
        RcMfsNode         mfs  = null;
        RcStreamPerformer pfmr = null;
        
        try {
            // RcMfsNode を生成し，遠隔操作を開始する．
            mfs = new RcMfsNode(SERVER_ADDR, SERVER_PORT, HANDLER);
            mfs.open();
            
            // ビデオとオーディオの入力デバイス情報リストを取得し，
            // 利用する入力デバイスを選択する．
            // （ここでは最初の入力デバイスを選択する．）
            RcDeviceInfoManager devInfMgr = mfs.getRcDeviceInfoManager();
            RcDeviceInfo        vidDev    = devInfMgr.getInputVideoDeviceInfoList().get(0);
            RcDeviceInfo        audDev    = devInfMgr.getInputAudioDeviceInfoList().get(0);
            
            // RcSegmentIo の入力を入力デバイスとして構成する．
            RcSegmentIo segIo = mfs.newRcSegmentIo();
            segIo.configureInputDevice(vidDev, audDev);
            
            // RcSegmentIo の出力をデフォルトレンダラとして構成する．
            segIo.configureDefaultRenderer();
            
            // ライブソースオプションを有効にする．
            segIo.setLiveSource(true);
            
            // RcSegmentIo をもとに RcStreamPerformer を生成する．
            pfmr = mfs.newRcStreamPerformer(segIo);
            
            // 入出力処理を開始する．
            pfmr.open();
            pfmr.start();
            
            // Enterキーの入力を待つ．
            System.out.printf("> Enter キーの入力を待ちます．");
            reader.readLine();
            
            // 入出力処理を終了する．
            pfmr.stop();
            pfmr.close();
        }
        catch (Exception ex) {
            // 遠隔操作中に例外が発生した．
            ex.printStackTrace();
        }
        finally {
            // RcStreamPerformer の処理を終了する．
            if (pfmr != null) {
                try {
                    pfmr.delete();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // RcMfsNode の処理を終了する．
            if (mfs != null) {
                mfs.close();
            }
            // ConsoleReader を解放する．
            reader.release();
        }
    }
    
// -----------------------------------------------------------------------------
// PRIVATE STATIC LAMBDA EXPRESSION:
// -----------------------------------------------------------------------------
    
    // - PRIVATE STATIC LAMBDA EXPRESSION --------------------------------------
    //    
    private static final ErrorResponseHandler HANDLER = (reason, response, ex) -> {
        // RPC応答処理時に発生したエラー情報を出力する．
        if (response != null) {
            Map<String, Object> error = response.getError();
            System.out.printf(
                "error: code: %s, message: %s, data: %s",
                error.get(ERR_CODE),
                error.get(ERR_MESSAGE),
                error.get(ERR_DATA)
            );
        }
        if (ex != null) {
            ex.printStackTrace();
        }
    };
}
