import java.io.File;

public class MainCounter_RQ2 {

	public static void main(String[] args) {
		if(args.length < 5){
			System.err.println("[ERROR] Missing Input File");
			System.exit(0);
		}
		
		System.out.println("Project;DataInicial;DataFinal;TypeBC;EnumBC;FieldBC;MethodBC;AllBC;TypeNBC;EnumNBC;FieldNBC;MethodNBC;AllNBC;TypeAdd;TypeRemoval;TypeModif;TypeDepOp"
				+ ";EnumAdd;EnumRemoval;EnumModif;EnumDepOp;FieldAdd;FieldRemoval;FieldModif;FieldDepOp;MethodAdd;MethodRemoval;MethodModif;MethodDepOp");
		
		File path1 = new File(args[3]);
		File path2 = new File(args[4]);
		String dataInicial = args [1];
		String dataFinal = args[2];
		String library = args[0];
		
		APIVersion version1 = new APIVersion(path1);
		APIVersion version2 = new APIVersion(path2);
		APIDiff diff = new APIDiff(version1, version2); //versao mais antiga - versao mais nova
		
		int typeBreakingChange = diff.getTypeDiff().getTypeBreakingChange();
		int typeNonBreakingChange = diff.getTypeDiff().getTypeNonBreakingChange();
		
		int typeAdd = diff.getTypeDiff().getTypeAdd();
		int typeRemoval = diff.getTypeDiff().getTypeRemoval();
		int typeModif = diff.getTypeDiff().getTypeModif();
		int typeDeprecatedOp = diff.getTypeDiff().getTypeDeprecatedOp();
		
		int fieldBreakingChange = diff.getFieldDiff().getFieldBreakingChange();
		int fieldNonBreakingChange = diff.getFieldDiff().getFieldNonBreakingChange();
		
		int fieldAdd = diff.getFieldDiff().getFieldAdd();
		int fieldRemoval = diff.getFieldDiff().getFieldRemoval();
		int fieldModif = diff.getFieldDiff().getFieldModif();
		int fieldDeprecatedOp = diff.getFieldDiff().getFieldDeprecatedOp();
		
		int methodBreakingChange = diff.getMethodDiff().getMethodBreakingChange();
		int methodNonBreakingChange = diff.getMethodDiff().getMethodNonBreakingChange();
		
		int methodAdd = diff.getMethodDiff().getMethodAdd();
		int methodRemoval = diff.getMethodDiff().getMethodRemoval();
		int methodModif = diff.getMethodDiff().getMethodModif();
		int methodDeprecatedOp = diff.getMethodDiff().getMethodDeprecatedOp();
		
		int enumBreakingChange = diff.getEnumDiff().getEnumBreakingChange() + diff.getEnumConstantDiff().getBreakingChange();
		int enumNonBreakingChange = diff.getEnumDiff().getEnumNonBreakingChange() + diff.getEnumConstantDiff().getNonBreakingChange();
		
		int enumAdd = diff.getEnumDiff().getEnunAdd() + diff.getEnumConstantDiff().getEnunConstantAdd();
		int enumRemoval = diff.getEnumDiff().getEnumRemoval() + diff.getEnumConstantDiff().getEnumConstantRemoval();
		int enumModif = diff.getEnumDiff().getEnumModif() + diff.getEnumConstantDiff().getEnumConstantModif();
		int enumDeprecatedOp = diff.getEnumDiff().getEnumDeprecatedOp() + diff.getEnumConstantDiff().getEnumConstantDeprecatedOp();
		
		int allBreakingChange = typeBreakingChange + fieldBreakingChange + methodBreakingChange + enumBreakingChange;
		int allNonBreakingChange = typeNonBreakingChange + fieldNonBreakingChange + methodNonBreakingChange + enumNonBreakingChange;
		
		
		
		//type bc, field bc, method bc, enum bc, total de changes (bc + nbc)
		System.out.println(library + ";" + dataInicial + ";" + dataFinal + ";" + typeBreakingChange + ";" + enumBreakingChange + ";" +
				fieldBreakingChange + ";" + methodBreakingChange + ";" + allBreakingChange + ";" + 
				typeNonBreakingChange+ ";" + enumNonBreakingChange + ";" + fieldNonBreakingChange + ";" + methodNonBreakingChange + ";" +
				allNonBreakingChange + ";" + typeAdd + ";" + typeRemoval + ";" + typeModif + ";" + typeDeprecatedOp + ";" + 
				enumAdd + ";" + enumRemoval + ";" + enumModif + ";" + enumDeprecatedOp + ";" + fieldAdd + ";" + 
				fieldRemoval + ";" + fieldModif + ";" + fieldDeprecatedOp + ";" + methodAdd + ";" + methodRemoval + ";" + 
				methodModif + ";" + methodDeprecatedOp);
	}

}
