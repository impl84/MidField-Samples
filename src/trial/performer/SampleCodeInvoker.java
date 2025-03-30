
package trial.performer;

import java.io.IOException;

import com.midfield_system.api.log.ConsolePrinter;
import com.midfield_system.api.log.ConsoleReader;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.11.05
 *
 */
public class SampleCodeInvoker
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    
    // サンプルコードのクラス名のデフォルト値
    private static final String SAMPLE_CLASS_NAME = "stream.DeviceToRendererEx";
    
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
        // サンプルコードのクラス名を決める．
        // 引数が 1つ以上有る場合は，最初の引数をサンプルコードのクラス名とする．
        // 引数で与えられていない場合は，デフォルト値を使う．
        String sampleClassName = SAMPLE_CLASS_NAME;
        if (args.length > 0) {
            sampleClassName = args[0];
        }
        SampleCodeInvoker invoker = null;
        try {
            // SampleCodeInvoker を生成し，サンプルコードの処理を開始する．
            invoker = new SampleCodeInvoker(sampleClassName);
            // SystemException
            
            // サンプルコードの終了のタイミングを待つ．
            invoker.waitForWindowClose();
            // IOException
        }
        catch (Exception ex) {
            // SampleCodeInvoker の実行中に例外が発生した．
            System.out.println("※SampleCodeInvoker の実行中に例外が発生しました．");
            ex.printStackTrace();
        }
        finally {
            // SampleCodeInvoker が生成されている場合は，終了処理を実行する．
            if (invoker != null) {
                invoker.close();
            }
        }
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    
    // MidField System
    private final MfsNode mfs;
    
    // サンプルコード
    private final AbstractSampleCode sample;
    
    // コンソールを利用した行単位の文字列の入出力
    private final ConsoleReader  reader;
    private final ConsolePrinter printer;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    SampleCodeInvoker(String sampleClassName)
        throws SystemException
    {
        // コンソールを利用した文字の入出力の準備をする．
        this.reader = ConsoleReader.getInstance();
        this.printer = ConsolePrinter.getInstance();
        
        this.printer.printf(
            "> SampleCodeInvoker の処理を開始します．(%s)\n",
            sampleClassName
        );
        // サンプルコードのインスタンスを生成し，保持する．
        this.sample = newSampleCode(sampleClassName);
        
        // MidField System を初期化し，起動する．
        this.printer.println("> MidField System を起動します．");
        this.mfs = MfsNode.initialize();	// SystemException
        this.mfs.activate();				// SystemException
        
        // 入出力構成ツールを生成する．
        ConfigTool cfgTool = new ConfigTool(this.reader, this.printer);
        
        // サンプルコードの処理を開始する．
        this.printer.println("> サンプルコードの処理を開始します．");
        this.sample.open(cfgTool);
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void waitForWindowClose()
        throws IOException
    {
        while (this.sample.isRunning()) {
            this.printer.println("> ウィンドウを閉じた後に Enter キーが押されるまで待ちます．");
            this.reader.readLine();
            // IOException
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void close()
    {
        // MidField System を終了する．
        if (MfsNode.isActive()) {
            this.printer.println("> MidField System を終了します．");
            this.mfs.shutdown();
        }
        // 標準入力からの1行読み込みに利用した ConsoleReader を解放する．
        this.reader.release();
        
        this.printer.println("> SampleCodeInvoker の処理を終了します．");
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private AbstractSampleCode newSampleCode(String sampleClassName)
    {
        AbstractSampleCode sample = null;
        
        // サンプルコードのインスタンスを生成する．
        try {
            // 発生する可能性のある例外は以下の通り．
            // forName():
            // ClassNotFoundException
            // getDeclaredConstructor():
            // NoSuchMethodException
            // newInstance():
            // InstantiationException,
            // IllegalAccessException,
            // InvocationTargetException
            sample = (AbstractSampleCode)Class.forName(sampleClassName)
                .getDeclaredConstructor()
                .newInstance();
        }
        catch (Exception ex) {
            // 上記各種の例外が発生する可能性がある一方，このメソッドとしては，
            // 不正な引数，または不適切な引数が渡されたことを
            // 呼び出し側に示すべきなので，
            // 発生した例外を原因とする IllegalArgumentException を生成して，
            // それを投げる．
            throw new IllegalArgumentException(ex);
        }
        // サンプルコードのインスタンスを返す．
        return sample;
    }
}
