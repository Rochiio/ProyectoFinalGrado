export class CalendarCreate {
  public mapsId: string;
  public listTasks: Array<TaskCreate>;

  constructor(){
    this.mapsId = '';
    this.listTasks = [];
  }
}

export class TaskCreate {
  public date: string;
  public task: string;

  constructor() {
    this.date = '';
    this.task = '';
  }
}
