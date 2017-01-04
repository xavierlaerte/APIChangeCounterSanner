import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;

public class EnumConstantDiff {
	
	private int breakingChange;
	private int nonBreakingChange;

	private int enunConstantAdd;
	private int enumConstantRemoval;
	private int enumConstantModif;
	private int enumConstantDeprecatedOp;

	public EnumConstantDiff(){
		this.breakingChange = 0;
		this.nonBreakingChange = 0;
		
		this.enunConstantAdd = 0;
		this.enumConstantRemoval = 0;
		this.enumConstantModif = 0;
		this.enumConstantDeprecatedOp = 0;
		
	}
	
	public int getEnunConstantAdd() {
		return enunConstantAdd;
	}


	public int getEnumConstantRemoval() {
		return enumConstantRemoval;
	}


	public int getEnumConstantModif() {
		return enumConstantModif;
	}


	public int getEnumConstantDeprecatedOp() {
		return enumConstantDeprecatedOp;
	}
	
	public int getBreakingChange() {
		return breakingChange;
	}


	public int getNonBreakingChange() {
		return nonBreakingChange;
	}


	public void calculateDiff(APIVersion version1, APIVersion version2) {
		this.findAddedConstant(version1, version2);
		this.findRemovedConstant(version1, version2);
		this.addedDeprecatedConstant(version1, version2);
	}

	private void findAddedConstant(APIVersion version1, APIVersion version2) {
		for(EnumDeclaration enumVersion2 : version2.getApiAccessibleEnums()){
			if(!Utils.isPrivate(enumVersion2)){
				EnumDeclaration enumVersion1 = version1.getVersionAccessibleEnum(enumVersion2);
				if(enumVersion1 != null && !Utils.isPrivate(enumVersion1)){
					for(Object constant : enumVersion2.enumConstants()){
						if(version1.getEqualVersionConstant((EnumConstantDeclaration) constant, enumVersion2) == null){
							this.nonBreakingChange++;
							this.enunConstantAdd++;
						}
					}
				} else {
					for(Object constant : enumVersion2.enumConstants()){
						this.nonBreakingChange++;
						this.enunConstantAdd++;
					}
				}
			}
		}
		
	}

	private void findRemovedConstant(APIVersion version1, APIVersion version2) {
		for(EnumDeclaration enumVersion1 : version1.getApiAccessibleEnums()){
			if(!Utils.isPrivate(enumVersion1)){
				EnumDeclaration enumVersion2 = version2.getVersionAccessibleEnum(enumVersion1);
				if(enumVersion2 != null && !Utils.isPrivate(enumVersion2)){
					for(Object constantVersion1 : enumVersion1.enumConstants()){
						if(version2.getEqualVersionConstant((EnumConstantDeclaration) constantVersion1, enumVersion1) == null){
							if(((EnumConstantDeclaration)constantVersion1).resolveVariable() != null &&
									((EnumConstantDeclaration)constantVersion1).resolveVariable().isDeprecated()){
								this.nonBreakingChange++;
								this.enumConstantDeprecatedOp++;
							} else {
								this.breakingChange++;
								this.enumConstantRemoval++;
							}
						}
					}
				} else {
					for(Object constantVersion1 : enumVersion1.enumConstants()){
						if(((EnumConstantDeclaration)constantVersion1).resolveVariable() != null &&
								((EnumConstantDeclaration)constantVersion1).resolveVariable().isDeprecated()){
							this.nonBreakingChange++;
							this.enumConstantDeprecatedOp++;
						} else {
							this.breakingChange++;
							this.enumConstantRemoval++;
						}
					}
				}
			}
		}
		
	}

	private void addedDeprecatedConstant(APIVersion version1, APIVersion version2) {
		for(EnumDeclaration enumVersion1 : version1.getApiAccessibleEnums()){
			if(!Utils.isPrivate(enumVersion1)){
				EnumDeclaration enumVersion2 = version2.getVersionAccessibleEnum(enumVersion1);
				if(enumVersion2 != null && !Utils.isPrivate(enumVersion2)){
					for(Object constantVersion2 : enumVersion2.enumConstants()){
						if(((EnumConstantDeclaration) constantVersion2).resolveVariable() != null &&
								((EnumConstantDeclaration) constantVersion2).resolveVariable().isDeprecated()){
							EnumConstantDeclaration constantVersion1 = version1.getEqualVersionConstant
									((EnumConstantDeclaration) constantVersion2, enumVersion2);
							if(constantVersion1 == null || (constantVersion1.resolveVariable() != null &&
									!constantVersion1.resolveVariable().isDeprecated()))
								this.nonBreakingChange++;
							
						}
					}
				}
			}
		}
		
	}
}
