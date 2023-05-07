import { UserDto } from "../user-dto/user-dto";

export class UserToken {
  public user: UserDto;
  public token: string;

  constructor(){
    this.user = new UserDto();
    this.token ='';
  }
}


