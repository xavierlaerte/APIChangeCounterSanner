import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class FieldDiff {
	
	private int fieldBreakingChange;
	private int fieldNonBreakingChange;
	
	private int fieldAdd;
	private int fieldRemoval;
	private int fieldModif;
	private int fieldDeprecatedOp;

	public int getFieldAdd() {
		return fieldAdd;
	}

	public int getFieldRemoval() {
		return fieldRemoval;
	}

	public int getFieldModif() {
		return fieldModif;
	}

	public int getFieldDeprecatedOp() {
		return fieldDeprecatedOp;
	}

	public FieldDiff() {
		this.fieldBreakingChange = 0;
		this.fieldNonBreakingChange = 0;
		
		this.fieldAdd = 0;
		this.fieldRemoval = 0;
		this.fieldModif = 0;
		this.fieldDeprecatedOp = 0;
	}
	
	public void calculateDiff(APIVersion version1, APIVersion version2) {
		this.findRemovedFields(version1, version2);
		this.findAddedFields(version1, version2);
		this.findAddedDeprecatedFields(version1, version2);
		this.findChangedVisibilityFields(version1, version2);
		this.findChangedTypeFields(version1, version2);
		this.findDefaultValueFields(version1, version2);
	}

	private void findDefaultValueFields(APIVersion version1, APIVersion version2) {
		for (TypeDeclaration type : version1.getApiAcessibleTypes()) {
			if(version2.contaisAccessibleType(type)){

				for (FieldDeclaration fieldInVersion1 : type.getFields()) {
					if(!Utils.isPrivate(fieldInVersion1)){
						FieldDeclaration fieldInVersion2;
						try {
							fieldInVersion2 = version2.getVersionField(fieldInVersion1, type);
						} catch (BindingException e) {
							continue;
						}
						if(fieldInVersion2 != null && !Utils.isPrivate(fieldInVersion2)){
							List<VariableDeclarationFragment> variable1Fragments = fieldInVersion1.fragments();
							List<VariableDeclarationFragment> variable2Fragments = fieldInVersion2.fragments();
							Expression valueVersion1 = null;
							Expression valueVersion2 = null;
							
							for (VariableDeclarationFragment variable1DeclarationFragment : variable1Fragments) {
								valueVersion1 = variable1DeclarationFragment.getInitializer();
							}
							
							for (VariableDeclarationFragment variable2DeclarationFragment : variable2Fragments) {
								valueVersion2 = variable2DeclarationFragment.getInitializer();
							}
							
							if(valueVersion1 == null && valueVersion2 != null){
								this.fieldBreakingChange++;
								this.fieldModif++;
							}
							else if(valueVersion1 != null && valueVersion2 == null){
								this.fieldModif++;
								this.fieldBreakingChange++;
							}
							else if(valueVersion1 != null && valueVersion2 != null)
								if(!valueVersion1.toString().equals(valueVersion2.toString())) {
									this.fieldModif++;
									this.fieldBreakingChange++;
							}
						}
					}
				}
			}
		}
	}

	private void findChangedTypeFields(APIVersion version1, APIVersion version2) {
		for (TypeDeclaration type : version1.getApiAcessibleTypes()) {
			if(version2.contaisAccessibleType(type)){

				for (FieldDeclaration fieldInVersion1 : type.getFields()) {
					if(!Utils.isPrivate(fieldInVersion1)){
						FieldDeclaration fieldInVersion2;
						try {
							fieldInVersion2 = version2.getVersionField(fieldInVersion1, type);
						} catch (BindingException e) {
							continue;
						}
						if(fieldInVersion2 != null && !Utils.isPrivate(fieldInVersion2)){
							if(!fieldInVersion1.getType().toString().equals(fieldInVersion2.getType().toString())){
								this.fieldBreakingChange++;
								this.fieldModif++;
							}
						}
					}
				}
			}
		}

	}

	private void findChangedVisibilityFields(APIVersion version1, APIVersion version2) {
		for (TypeDeclaration type : version1.getApiAcessibleTypes()) {
			if(version2.contaisAccessibleType(type)){

				for (FieldDeclaration fieldInVersion1 : type.getFields()) {
					FieldDeclaration fieldInVersion2;
					try {
						fieldInVersion2 = version2.getVersionField(fieldInVersion1, type);
					} catch (BindingException e) {
						continue;
					}
					if(fieldInVersion2 != null){
						if(Utils.isPrivate(fieldInVersion1) && !Utils.isPrivate(fieldInVersion2)){
							this.fieldNonBreakingChange++;
						} else if(!Utils.isPrivate(fieldInVersion1) && Utils.isPrivate(fieldInVersion2)){
							if(Utils.isDeprecatedField(fieldInVersion1)){
								this.fieldNonBreakingChange++;
								this.fieldDeprecatedOp++;
							} else {
								this.fieldBreakingChange++;
								this.fieldModif++;
							}
						}
					}
				}
			}
		}
	}

	private void findAddedDeprecatedFields(APIVersion version1, APIVersion version2) {
		for (TypeDeclaration type : version1.getApiAcessibleTypes()) {
			if(version2.contaisAccessibleType(type)){

				for (FieldDeclaration fieldInVersion1 : type.getFields()) {
					if(!Utils.isPrivate(fieldInVersion1)){
						FieldDeclaration fieldInVersion2;
						try {
							fieldInVersion2 = version2.getVersionField(fieldInVersion1, type);
						} catch (BindingException e) {
							continue;
						}
						if(fieldInVersion2 != null && !Utils.isPrivate(fieldInVersion2)){
							//os dois tipos tem os mesmos fields

							if((!Utils.isDeprecatedField(fieldInVersion1)) && (Utils.isDeprecatedField(fieldInVersion2))){
								this.fieldNonBreakingChange++;
							}
						}
					}
				}
			}	
		}

		for (TypeDeclaration type : version2.getApiAcessibleTypes()) {
			for (FieldDeclaration fieldInVersion2 : type.getFields()) {
				if(!Utils.isPrivate(fieldInVersion2)){
					FieldDeclaration fieldInVersion1;
					try {
						fieldInVersion1 = version1.getVersionField(fieldInVersion2, type);
					} catch (BindingException e) {
						continue;
					}
					if((fieldInVersion1 == null) && (Utils.isDeprecatedField(fieldInVersion2))){
						this.fieldNonBreakingChange++;
					}
				}
			}
		}
	}

	public int getFieldBreakingChange() {
		return fieldBreakingChange;
	}

	public int getFieldNonBreakingChange() {
		return fieldNonBreakingChange;
	}

	private void findAddedFields(APIVersion version1, APIVersion version2) {
		for (TypeDeclaration type : version2.getApiAcessibleTypes()) {
			if(version1.contaisAccessibleType(type)){

				for (FieldDeclaration fieldInVersion2 : type.getFields()) {
					if(!Utils.isPrivate(fieldInVersion2)){
						FieldDeclaration fieldInVersion1;
						try {
							fieldInVersion1 = version1.getVersionField(fieldInVersion2, type);
						} catch (BindingException e) {
							continue;
						}
						if(fieldInVersion1 == null){
							this.fieldNonBreakingChange++; //added field
							this.fieldAdd++;
						}
					}
				}
			} else{
				//tipo foi adicionado na versao 2, todos os fields foram adicionados
				for (FieldDeclaration field : type.getFields()) {
					if(!Utils.isPrivate(field)){
						this.fieldNonBreakingChange++; //addded field
						this.fieldAdd++;
					}
				}
			}
		}
	}

	private void findRemovedFields(APIVersion version1, APIVersion version2) {
		for (TypeDeclaration type : version1.getApiAcessibleTypes()) {
			if(version2.contaisAccessibleType(type)){
				for (FieldDeclaration fieldInVersion1 : type.getFields()) {

					if(!Utils.isPrivate(fieldInVersion1)){
						FieldDeclaration fieldInVersion2;
						try {
							fieldInVersion2 = version2.getVersionField(fieldInVersion1, type);
						} catch (BindingException e) {
							continue;
						}
						if(fieldInVersion2 == null){
							if(Utils.isDeprecatedField(fieldInVersion1)){
								this.fieldNonBreakingChange++; //removed deprecated field
								this.fieldDeprecatedOp++;
							} else{
								this.fieldBreakingChange++; //removed field
								this.fieldRemoval++;
							}
						}
					}

				}
			} else{ 
				//tipo foi removido na versao 2, todos os fields foram removidos
				for (FieldDeclaration fieldInVersion1 : type.getFields()) {
					if(!Utils.isPrivate(fieldInVersion1)){
						if(Utils.isDeprecatedField(fieldInVersion1)){
							this.fieldNonBreakingChange++; //removed deprecated field
							this.fieldDeprecatedOp++;
						} else{
							this.fieldBreakingChange++; //removed field
							this.fieldRemoval++;
						}
					}
				}
			}
		}
	}
}
