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

  constructor() {
    this.username = '';
    this.message = '';
  }
}
