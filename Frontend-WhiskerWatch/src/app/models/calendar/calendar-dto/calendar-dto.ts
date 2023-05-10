export class CalendarDto {
  public id: string;
  public mapsId: string;
  public listTasks: Array<TaskDto>;

  constructor() {
    this.id = '';
    this.mapsId = '';
    this.listTasks = [];
  }
}

export class TaskDto {
  public id: string;
  public date: Date;
  public task: string;

  constructor() {
    this.id = '';
    this.date = new Date();
    this.task = '';
  }
}
