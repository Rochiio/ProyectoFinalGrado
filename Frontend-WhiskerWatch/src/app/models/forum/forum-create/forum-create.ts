export class ForumCreate {
  public mapsId: string;
  public listMessages: Array<ForumMessagesCreate>;

  constructor() {
    this.mapsId = '';
    this.listMessages = [];
  }
}

export class ForumMessagesCreate {
  public username: string;
  public message: string;
  public created_At: string;

  constructor() {
    this.username = '';
    this.message = '';
    this.created_At = '';
  }
}
