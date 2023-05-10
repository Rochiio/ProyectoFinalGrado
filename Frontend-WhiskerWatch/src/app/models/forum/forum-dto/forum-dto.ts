export class ForumDto {
  public id: string;
  public mapsId: string;
  public listMessages: Array<ForumMessagesDto>;

  constructor(){
    this.id = '';
    this.mapsId = '';
    this.listMessages = [];
  }
}

export class ForumMessagesDto {
  public id: string;
  public username: string;
  public message: string;
  public created_At: Date;

  constructor() {
    this.id = '';
    this.username = '';
    this.message = '';
    this.created_At = new Date();
  }
}
