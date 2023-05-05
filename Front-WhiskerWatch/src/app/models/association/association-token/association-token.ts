import { AssociationDto } from "../association-dto/association-dto";

export class AssociationToken {
  public user: AssociationDto;
  public token: string;

  constructor(){
    this.user = new AssociationDto();
    this.token ='';
  }
}
