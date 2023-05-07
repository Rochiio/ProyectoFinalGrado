export class AssociationCreate {
  public name: string;
  public email: string;
  public username: string;
  public password: string;
  public rol: string;
  public description: string;
  public url: string;

  constructor(){
    this.name ='';
    this.email ='';
    this.username ='';
    this.password = '';
    this.rol = 'ASSOCIATION';
    this.description = '';
    this.url = '';
  }
}
