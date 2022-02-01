
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: ConsoleReader
 *
 * Date Modified: 2021.08.23
 *
 */
public class ConsoleReader
	implements	LineReader
{
// =============================================================================
//  CLASS VARIABLE:
// =============================================================================
	
	// - PRIVATE STATIC VARIABLE -----------------------------------------------
	private static ConsoleReader theInstance = null;
	
// =============================================================================
//  CLASS METHOD:
// =============================================================================
	
// -----------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
	
	// - PUBLIC STATIC METHOD --------------------------------------------------
	//
	public static ConsoleReader getInstance()
	{
		// ConsoleReader のインスタンスの有無を確認する．
		if (ConsoleReader.theInstance == null) {
			// インスタンスが存在しない場合は，
			// ConsoleReader のインスタンスを生成する．
			ConsoleReader.theInstance = new ConsoleReader();
		}
		// ConsoleReader のインスタンスを返す．
		return ConsoleReader.theInstance;
		
	}
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================
	
	// - PRIVATE VARIABLE ------------------------------------------------------
	
	// 標準入力からの1行読み込みに利用する BufferedReader
	private BufferedReader bufferedReader = null;
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD: 
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	//
	public void release()
	{
		try {
			// BufferedReader のインスタンスの有無を確認する．
			if (this.bufferedReader == null) {
				// インスタンスが存在しないので，何もせずに戻る．
				return;
			}
			// BufferedReader を閉じる．
			this.bufferedReader.close();
				// IOException
		}
		catch (IOException ex) {
			// 例外発生時のスタックトレースを出力する．
			ex.printStackTrace();
		}
		finally {
			// BufferedReader のインスタンスを格納するための
			// 変数を初期化しておく．
			this.bufferedReader = null;
		}
	}
	
// -----------------------------------------------------------------------------
//  PUBLIC METHOD: IMPLEMENTS: LineReader
// -----------------------------------------------------------------------------
	
	// - PUBLIC METHOD ---------------------------------------------------------
	// - IMPLEMENTS: LineReader
	//
	@Override
	public String readLine()
		throws	IOException
	{
		// 標準入力から1行読み込む．
		String line = this.bufferedReader.readLine();
			// IOException
		
		// 読み込んだ1行を返す．
		return line;
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private ConsoleReader()
	{
		// 標準入力からの1行読み込みに利用する BufferedReader を生成する．
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
}
