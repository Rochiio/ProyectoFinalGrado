export class UserDto {
  public id: string;
  public name: string;
  public email: string;
  public username: string;
  public rol: string;

  constructor() {
    this.id = '';
    this.name = '';
    this.email = '';
    this.username = '';
    this.rol = '';
  }
}
