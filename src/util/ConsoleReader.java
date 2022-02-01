
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
		// ConsoleReader �̃C���X�^���X�̗L�����m�F����D
		if (ConsoleReader.theInstance == null) {
			// �C���X�^���X�����݂��Ȃ��ꍇ�́C
			// ConsoleReader �̃C���X�^���X�𐶐�����D
			ConsoleReader.theInstance = new ConsoleReader();
		}
		// ConsoleReader �̃C���X�^���X��Ԃ��D
		return ConsoleReader.theInstance;
		
	}
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================
	
	// - PRIVATE VARIABLE ------------------------------------------------------
	
	// �W�����͂����1�s�ǂݍ��݂ɗ��p���� BufferedReader
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
			// BufferedReader �̃C���X�^���X�̗L�����m�F����D
			if (this.bufferedReader == null) {
				// �C���X�^���X�����݂��Ȃ��̂ŁC���������ɖ߂�D
				return;
			}
			// BufferedReader �����D
			this.bufferedReader.close();
				// IOException
		}
		catch (IOException ex) {
			// ��O�������̃X�^�b�N�g���[�X���o�͂���D
			ex.printStackTrace();
		}
		finally {
			// BufferedReader �̃C���X�^���X���i�[���邽�߂�
			// �ϐ������������Ă����D
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
		// �W�����͂���1�s�ǂݍ��ށD
		String line = this.bufferedReader.readLine();
			// IOException
		
		// �ǂݍ���1�s��Ԃ��D
		return line;
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private ConsoleReader()
	{
		// �W�����͂����1�s�ǂݍ��݂ɗ��p���� BufferedReader �𐶐�����D
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
}
