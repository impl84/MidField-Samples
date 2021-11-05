
package communicator;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemProperty;

import util.ConsolePrinter;
import util.ConsoleReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: TwoWayCommunication 
 *
 * Date Modified: 2021.10.27
 *
 */

//==============================================================================
public class TwoWayCommunication
{
	//- PACKAGE CONSTANT VALUE -------------------------------------------------
	static final String ASYNC_INTERVIEW_REQUEST		= "Async-Interview-Request";
	static final String ASYNC_INTERVIEW_RESPONSE	= "Async-Interview-Response";
	static final String INTERVIEW_REQUEST			= "Interview-Request";
	static final String INTERVIEW_RESPONSE			= "Interview-Response";
	
//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void main(String[] args)
	{
		// �R���\�[���𗘗p���������̓��o�͂̏���������D
		ConsoleReader reader = ConsoleReader.getInstance();
		ConsolePrinter printer = ConsolePrinter.getInstance();
		
		MfsNode mfs = null;
		try {
			// MidField System �����������C�N������D
			printer.println("> MidField System ���N�����܂��D");
			mfs = MfsNode.initialize();	// SystemException
			mfs.activate();				// SystemException
			
			// MidField System �����p���Ă��郍�[�J��IP�A�h���X���擾����D
			SystemProperty sp = SystemProperty.getCurrentProperty();
			String localIpAddr = sp.getSysLocalIpAddrWoScopeId();
			
			// Responder �� Interviewer �𐶐�����D
			Responder responder = new Responder(printer);
			Interviewer interviewer = new Interviewer(printer, localIpAddr);
			
			// Interviewer �� Responder �֗v���p�P�b�g�𑗐M���C
			// Responder �� Interviewer �։����p�P�b�g��ԐM����D
			// ��Interviewer �͗v���p�P�b�g�𑗐M������C
			//   �قȂ�X���b�h�ŕԐM����������D
			interviewer.asyncInterview();
			
			// Interviewer �� Responder �֗v���p�P�b�g�𑗐M���C
			// Responder �� Interviewer �։����p�P�b�g��ԐM����D
			// ��Interviewer �͗v���p�P�b�g�𑗐M������C
			//   �����X���b�h�ŕԐM���͂��܂ő҂��C�ԐM����������D
			interviewer.interview();
			
			// Enter �L�[�̓��͂�҂D
			printer.println("> Enter �L�[�������Ă��������D");
			reader.readLine();
			
			// Interviewer �� Responder ���I������D
			interviewer.close();
			responder.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			// MidField System ���I������D
			if (mfs != null) {
				System.out.println("> MidField System ���I�����܂��D");
				mfs.shutdown();
			}
			// �W�����͂����1�s�ǂݍ��݂ɗ��p���� ConsoleReader ���������D
			reader.release();
		}
	}
}
