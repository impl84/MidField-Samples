
package performer;

import java.io.IOException;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;

import util.ConsolePrinter;
import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: SimpleVideoChat
 *
 * Date Modified: 2021.11.05
 *
 */
public class SimpleVideoChat
{
// =============================================================================
// CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - MAIN METHOD -----------------------------------------------------------
    //
    public static void main(String[] args)
    {
        MfsNode            mfs      = null;
        AbstractSampleCode sender   = null;
        AbstractSampleCode receiver = null;
        ConsoleReader      reader   = ConsoleReader.getInstance();
        ConsolePrinter     printer  = ConsolePrinter.getInstance();
        
        try {
            // MidField System を初期化する．
            mfs = MfsNode.initialize();		// SystemException
            
            // MidField System を起動する．
            mfs.activate();					// SystemException
            
            // 入出力構成ツールを生成する．
            ConfigTool cfgTool = new ConfigTool(reader, printer);
            
            // ビデオ送信用のサンプルコードを実行する．
            sender = new DeviceToNetworkEx();
            sender.open(cfgTool);		// SystemException, StreamException
            
            // ビデオ受信用のサンプルコードを実行する．
            receiver = new NetworkToRendererEx();
            receiver.open(cfgTool);
            
            // Enterキーの入力を待つ．
            System.out.print("> Enter キーの入力を待ちます．");
            reader.readLine();	// IOException
        }
        catch (SystemException ex) {
            // MidField System の内部処理時に例外が発生した．
            // 例外発生時のスタックトレースを表示する．
            ex.printStackTrace();
        }
        catch (IOException ex) {
            // Enterキー入力時に例外が発生した．または上記以外の例外が発生した．
            // 例外発生時のスタックトレースを表示する．
            ex.printStackTrace();
        }
        finally {
            // サンプルコードの処理を終了する．
            if (receiver != null) { receiver.close(); }
            if (sender != null) { sender.close(); }
            
            // MidField System を終了する．
            if (mfs != null) { mfs.shutdown(); }
            
            // 標準入力からの1行読み込みに利用した ConsoleReader を解放する．
            reader.release();
        }
    }
}