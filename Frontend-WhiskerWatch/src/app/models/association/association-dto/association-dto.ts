export class AssociationDto {
  public id: string;
  public name: string;
  public email: string;
  public username: string;
  public password: string;
  public rol: string;
  public description: string;
  public url: string;
  public img: string;

  constructor(){
    this.id = '';
    this.name ='';
    this.email ='';
    this.username ='';
    this.password = '';
    this.rol = 'ASSOCIATION';
    this.description = '';
    this.url = '';
    this.img = '';
  }
}
