import { AssociationDto } from "../association-dto/association-dto";

export class AssociationToken {
  public association: AssociationDto;
  public token: string;

  constructor(){
    this.association = new AssociationDto();
    this.token ='';
  }
}
