export class UserCreate {
  public name: string;
  public email: string;
  public password: string;
  public username: string;
  public rol: string;

  constructor(){
    this.name ='';
    this.email ='';
    this.password ='';
    this.username ='';
    this.rol ='USER';
  }
}
