
package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.IoParam;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.util.LogPrinter;
import com.midfield_system.protocol.StreamInfo;

import util.LineReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: ConsoleMenu
 *
 * Date Modified: 2021.08.19
 *
 */

//==============================================================================
abstract class ConsoleMenu
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		STR_ITEM_LIST		= "��",
		STR_ITEM_MESSAGE	= "�|",
		STR_ITEM_WARNING	= "��";
	
//==============================================================================
//  CLASS VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private static LogPrinter printer = null;
	private static LineReader reader = null;
	
	private static SegmentIo segmentIo = new SegmentIo();
	private static StreamPerformer performer = null;
	
//==============================================================================
//  CLASS METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	static void setLogPrinter(LogPrinter printer)
	{
		ConsoleMenu.printer = printer;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	static void setLineReader(LineReader reader)
	{
		ConsoleMenu.reader = reader;
	}
	
//------------------------------------------------------------------------------
//  PROTECTED METHOD:
//------------------------------------------------------------------------------
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	static protected SegmentIo getSegmentIo()
	{
		return ConsoleMenu.segmentIo;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	static protected void setSelectedStream(StreamPerformer pfmr)
	{
		ConsoleMenu.performer = pfmr;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	static protected StreamPerformer getSelectedStream()
	{
		return ConsoleMenu.performer;
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private static void printf(String format, Object...args)
	{
		if (ConsoleMenu.printer != null) {
			ConsoleMenu.printer.printf(format, args);
		}
		else {
			System.out.printf(format, args);
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private static String readLine()
		throws	IOException
	{
		String line = null;
		
		if (ConsoleMenu.reader != null) {
			line = ConsoleMenu.reader.readLine();
				// IOException
		}
		else {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in)
			);
			line = reader.readLine();
				// IOException
		}
		return line;
	}
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private MenuItem[] menuItems = null;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================

//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	ConsoleMenu()
	{
		this.menuItems = initMenuItems();
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	ConsoleMenu action()
	{
		ConsoleMenu nextMenu = this;
		
		// ���j���[�^�C�g�����o�͂���D
		printSeparator();
		print("%s\n", getMenuTitle());
		printShortSeparator();
		print("\n");
		
		// ���j���[���ڂ��o�͂���D
		for (MenuItem item : this.menuItems) {
			if (item.isEmptyMenu() == false) {
				print("  [%d] %s\n", item.getMenuNumber(), item.getDescription());
			}
			else {
				print("\n");
			}
		}
		// ���j���[�ԍ��̓��͂�҂D
		int selectedNumber = selectNumber("���j���[�ԍ�");
		
		// �I�����ꂽ���j���[�ԍ��ɑΉ������A�N�V���������s����D
		for (MenuItem item : this.menuItems) {
			if (selectedNumber == item.getMenuNumber()) {
				nextMenu = item.action();
				break;
			}
		}
		// ���� ConsoleMenu ��Ԃ��D
		return nextMenu;
	}
	
//------------------------------------------------------------------------------
//  PROTECTED METHOD: �g���N���X�Ŏ������郁�\�b�h
//------------------------------------------------------------------------------

	//- PROTECTED METHOD -------------------------------------------------------
	//
	abstract protected MenuItem[] initMenuItems();
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	abstract protected String getMenuTitle();
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	abstract protected String getPrompt();
	
//------------------------------------------------------------------------------
//  PROTECTED METHOD: ���b�Z�[�W�̏o��
//------------------------------------------------------------------------------

	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void print(String format, Object ... args)
	{
		ConsoleMenu.printf("[%s] ", getPrompt());
		ConsoleMenu.printf(format, args);
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void message(String format, Object ... args)
	{
		ConsoleMenu.printf("[%s] %s", getPrompt(), STR_ITEM_MESSAGE);
		ConsoleMenu.printf(format, args);
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void warning(String format, Object ... args)
	{
		ConsoleMenu.printf("[%s] %s", getPrompt(), STR_ITEM_WARNING);
		ConsoleMenu.printf(format, args);
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void printListTitle(String title)
	{
		print("%s%s\n", STR_ITEM_LIST, title);
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void printDeviceInfoList(List<DeviceInfo> lsDevInf)
	{
		printListTitle("���̓f�o�C�X�ꗗ");
		int size = lsDevInf.size();
		for (int i = 0; i < size; i++) {
			DeviceInfo devInf = lsDevInf.get(i);
			print("  [%s] %s\n", Integer.toString(i), devInf.getIoName());
		}
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void printStreamFormatList(List<StreamFormat> lsStmFmt)
	{
		printListTitle("�t�H�[�}�b�g�ꗗ");
		int size = lsStmFmt.size();
		for (int i = 0; i < size; i++) {
			StreamFormat stmFmt = lsStmFmt.get(i);
			print("  [%s] %s\n", Integer.toString(i), stmFmt.getDescription());
		}
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void printIoParamList(String msg, List<IoParam> lsIoPrm)
	{
		printListTitle(msg);
		int size = lsIoPrm.size();
		for (int i = 0; i < size; i++) {
			print("  [%s] ", Integer.toString(i));
			IoParam ioMap = lsIoPrm.get(i);
			printIoParam(ioMap);
		}
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void printStreamInfoList(String msg, List<StreamInfo> lsOutInf)
	{
		printListTitle(msg);
		int size = lsOutInf.size();
		for (int i = 0; i < size; i++) {
			print("  [%s] ", Integer.toString(i));
			StreamInfo outInf = lsOutInf.get(i);
			printStreamInfo(outInf);
		}
	}
	
//------------------------------------------------------------------------------
//  PROTECTED METHOD: ���͂𔺂����b�Z�[�W�̏o��
//------------------------------------------------------------------------------
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected int selectNumber(String msg)
	{
		int num = -1;
		
		print("\n");
		print("  [��L�ȊO�̔ԍ�] �L�����Z��\n");
		
		while (num < 0) {
			try {
				print("\n");
				print("> %s����͂��Ă��������D\n", msg);
				print("> ");
				String line = ConsoleMenu.readLine();
					// IOException
				
				ConsoleMenu.printf("%s\n", line);
				print("\n");
				
				num = Integer.parseInt(line);
					// NumberFormatException
			}
			catch (NumberFormatException ex) {
				continue;
			}
			catch (IOException ex) {
				warning(ex.getMessage());
				break;
			}
		}
		printShortSeparator();
		
		return num;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected String getLine(String msg)
	{
		String line = null;
		
		try {
			print("> %s����͂��Ă��������D\n", msg);
			print("> ");
			line = ConsoleMenu.readLine();
				// IOException
			
			ConsoleMenu.printf("%s\n", line);
			print("\n");
		}
		catch (IOException ex) {
			warning(ex.getMessage());
		}
		return line;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void messagePause(String format, Object ... args)
	{
		message(format, args);
		pause();
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void warningPause(String format, Object ... args)
	{
		warning(format, args);
		pause();
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void pause()
	{
		print("> ENTER KEY �������Ă��������D\n");
		try {
			ConsoleMenu.readLine();
		}
		catch (IOException ex) {
			warning(ex.getMessage());
		}
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected ConsoleMenu showSegmentIo()
	{
		SegmentIo segIo = ConsoleMenu.getSegmentIo();
		printSegmentIo(segIo);
		
		return this;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	protected void printSegmentIo(SegmentIo segIo)
	{
		print("\n");
		print("�����o�͐ݒ�  �`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`\n");
		printIoParamList("���͐ݒ�", segIo.getInputParamList());
		printIoParamList("�o�͐ݒ�", segIo.getOutputParamList());
		print("�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`\n");
		pause();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printSeparator()
	{
		ConsoleMenu.printf("\n");
		//                             1         2         3         4
		//                   01234567890123456789012345678901234567890
		ConsoleMenu.printf("�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\");
		ConsoleMenu.printf("�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\�\\n");
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printShortSeparator()
	{
		//                             1         2         3         4
		//                   01234567890123456789012345678901234567890
		print(              "- - - - - - - - - - - - - - - - - - - - ");
		ConsoleMenu.printf("- - - - - - - - - - - - - - - - \n");
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printIoParam(IoParam ioPrm)
	{
		// Description ��\������D
		String desc = ioPrm.getDescription();
		if (desc == null) {
			desc = ioPrm.getStreamName();
		}
		ConsoleMenu.printf("%s\n", desc);
		
		// ���M�z�X�g��\������D
		String sender = ioPrm.getSourceAddress();
		if (sender != null) {
			print( "    ���M�z�X�g  : %s\n", sender);
		}
		// �v���g�R����\������D
		String prot = ioPrm.getConnectionInfo();
		if (prot != null) {
			print( "    �v���g�R��  : %s\n", prot);
		}
		// �t�H�[�}�b�g��\������D
		StreamFormat stmFmt = ioPrm.getStreamFormat();
		print( "    �t�H�[�}�b�g: %s\n", stmFmt.toString());
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printStreamInfo(StreamInfo stmInf)
	{
		// StreamInfo ���� IoParam �̃��X�g�𕜌�����D
		List<IoParam> lsIoPrm = stmInf.restoreIoParamList();
		
		// IoParam �̃��X�g�擪�̗v�f���擾���C�ݒ荀�ڂ�\������D
		printIoParam(lsIoPrm.get(0));
		
		// IoParam �̃��X�g�� 2�߂̗v�f������ꍇ�́C
		// ���̃t�H�[�}�b�g��\������D
		if (lsIoPrm.size() >= 2) {
			IoParam ioPrm = lsIoPrm.get(1);
			StreamFormat stmFmt = ioPrm.getStreamFormat();
			print( "    �t�H�[�}�b�g: %s\n", stmFmt.toString());
		}
	}
}
