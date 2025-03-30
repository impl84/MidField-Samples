
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
public class SampleCodeSelector
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    
    // selectSampleCode メソッドの結果を示す定数
    private static final int END_OF_SELECTOR     = -1;
    private static final int INDEX_OUT_OF_BOUNDS = -2;
    
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
        SampleCodeSelector selector = null;
        try {
            // SampleCodeSelector を生成する．
            selector = new SampleCodeSelector();
            // SystemException
            
            // サンプルコードの実行と終了を繰り返す．
            selector.mainLoop();
        }
        catch (Exception ex) {
            // SampleCodeSelector の動作中に例外が発生した．
            System.out.println("※SampleCodeSelector の実行中に例外が発生しました．");
            ex.printStackTrace();
        }
        finally {
            // SampleCodeSelector が生成されている場合は，終了処理を実行する．
            if (selector != null) {
                selector.close();
            }
        }
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    
    // MidField System
    private final MfsNode mfs;
    
    // 入出力構成ツールを生成する．
    private final ConfigTool cfgTool;
    
    // サンプルコードとして実行可能なインスタンスの配列
    private final AbstractSampleCode[] samples;
    
    // コンソールを利用した文字の入出力
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
    SampleCodeSelector()
        throws SystemException
    {
        // コンソールを利用した文字の入出力の準備をする．
        this.reader = ConsoleReader.getInstance();
        this.printer = ConsolePrinter.getInstance();
        
        this.printer.println("> SampleCodeSelector の処理を開始します．");
        
        // MidField System を初期化し，起動する．
        this.printer.println("> MidField System を起動します．");
        this.mfs = MfsNode.initialize();	// SystemException
        this.mfs.activate();				// SystemException
        
        // 入出力構成ツールを生成する．
        this.cfgTool = new ConfigTool(this.reader, this.printer);
        
        // サンプルコードとして実行可能なインスタンスを要素とする配列を生成する．
        this.samples = new AbstractSampleCode[] {
            new DeviceToRendererEx(),
            new DeviceToNetworkEx(),
            new NetworkToRendererEx(),
            new NetworkToNetworkEx()
        };
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void mainLoop()
    {
        // サンプルコードを選択し，開始または停止を繰り返す．
        while (true) {
            // サンプルコードのリストを表示する．
            printSampleCodeList();
            
            // サンプルコードを選択する．
            int result = selectSampleCode();
            if (result == END_OF_SELECTOR) {
                // 終了番号が入力されたのでループを抜ける．
                break;
            }
            else if (result == INDEX_OUT_OF_BOUNDS) {
                // 範囲外の番号が入力されたので次のループの処理に入る．
                continue;
            }
            // 入力された番号のサンプルコードの処理を開始または終了する．
            changeRunningState(result);
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    void close()
    {
        // MidField System を終了する．
        if (MfsNode.isActive()) {
            // 処理を終了していないサンプルコードがある場合は終了させる．
            closeAllSampleCodes();
            
            this.printer.println("> MidField System を終了します．");
            this.mfs.shutdown();
        }
        // 標準入力からの1行読み込みに利用した ConsoleReader を解放する．
        this.reader.release();
        
        this.printer.println("> SampleCodeSelector の処理を終了します．");
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void printSampleCodeList()
    {
        // サンプルコードの一覧を表示する．
        this.printer.println("");
        this.printer.println("▼サンプルコード一覧：");
        
        // サンプルコードとして実行可能なインスタンスの配列を走査する．
        for (int n = 0; n < this.samples.length; n++) {
            // サンプルコードのインスタンスを取得する．
            AbstractSampleCode sample = this.samples[n];
            
            // 動作状態に応じた出力文字列を決める．
            String runningState = null;
            if (sample.isRunning()) {
                runningState = "動作中";
            }
            else {
                runningState = "停止中";
            }
            // サンプルコードの番号・状態・説明を出力する．
            this.printer.printf(
                " [%02d] (%s) %s\n", n,
                runningState,
                sample.getDescription()
            );
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private int selectSampleCode()
    {
        int res = 0;
        
        // サンプルコードを選択し，開始/停止する．
        try {
            // サンプルコード番号をキーボードから取得する．
            this.printer.printf(
                "> 番号入力[0-%d: 実行，%d: 終了]：",
                this.samples.length - 1, this.samples.length
            );
            String line = this.reader.readLine();	// IOException
            int    n    = Integer.parseInt(line);			// NumberFormatException
            
            // 入力された値の範囲を確認する．
            if ((n >= 0) && (n < this.samples.length)) {
                // 選択されたサンプルコードの配列のインデックス値を
                // リザルトコードに設定する．
                res = n;
            }
            else if (n == this.samples.length) {
                // 終了を意味する番号が入力されたので，
                // リザルトコードに END_OF_SELECTOR を設定する．
                this.printer.println("  プログラムを終了します．");
                res = END_OF_SELECTOR;
            }
            else {
                // リザルトコードに INDEX_OUT_OF_BOUNDS を設定する．
                this.printer.println("  ※適切な番号を入力してください．");
                res = INDEX_OUT_OF_BOUNDS;
            }
        }
        catch (NumberFormatException ex) {
            // NumberFormatException が発生した場合は，
            // リザルトコードに INDEX_OUT_OF_BOUNDS を設定する．
            this.printer.printf(
                "  ※適切な番号を入力してください．(%s)\n",
                ex.getMessage()
            );
            res = INDEX_OUT_OF_BOUNDS;
        }
        catch (IOException ex) {
            // IOException が発生した場合は，例外メッセージを表示して，
            // リザルトコードに END_OF_SELECTOR を設定する．
            this.printer.println("※キーボードからの入力処理で例外が発生しました．");
            ex.printStackTrace();
            res = END_OF_SELECTOR;
        }
        return res;
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void changeRunningState(int idx)
    {
        // 選択されたサンプルコードのインスタンスを取得する．
        AbstractSampleCode selectedSample = this.samples[idx];
        
        // サンプルコードの動作状態により，
        // サンプルコードの処理を開始または終了する．
        if (selectedSample.isRunning()) {
            // サンプルコードの処理を終了する．
            selectedSample.close();
        }
        else {
            // サンプルコードの処理を開始する．
            selectedSample.open(this.cfgTool);
        }
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void closeAllSampleCodes()
    {
        // サンプルコードとして実行可能なインスタンスの配列を走査する．
        for (int n = 0; n < this.samples.length; n++) {
            // サンプルコードのインスタンスを取得する．
            AbstractSampleCode sample = this.samples[n];
            
            // 処理を終了していないサンプルコードがある場合は処理を終了させる．
            if (sample.isRunning()) {
                sample.close();
            }
        }
    }
}