import { Injectable } from '@angular/core';
import { ForumMessagesCreate } from '../models/forum/forum-create/forum-create';
import { ForumMessagesDto } from '../models/forum/forum-dto/forum-dto';
import { TaskDto } from '../models/calendar/calendar-dto/calendar-dto';
import { TaskCreate } from '../models/calendar/calendar-create/calendar-create';
import { EventsCalendarInt } from '../models/fullcalendar/events-calendar-int';

@Injectable({
  providedIn: 'root'
})
export class MappersService {

  constructor() { }

  public forumMessagesDtoToCreate(list: ForumMessagesDto[]): ForumMessagesCreate[]{
    let listMapper: ForumMessagesCreate[] = [];

    list.forEach((message : ForumMessagesDto) => {
      let mapper = new ForumMessagesCreate();
      mapper.message = message.message;
      mapper.username = message.username;
      mapper.created_At = message.created_At;
      listMapper.push(mapper);
    });

    return listMapper;
  }

  public calendarTasksDtoToCreate(listTasks: TaskDto[]): TaskCreate[]{
    let listMapper: TaskCreate[] = [];

    listTasks.forEach((message : TaskDto) => {
      let mapper = new TaskCreate();
      mapper.date = message.date;
      mapper.task = message.task;
      listMapper.push(mapper);
    });

    return listMapper;
  }

  public eventsToCalendarInt(tasks :TaskDto[]) : EventsCalendarInt[]{
    let events!: EventsCalendarInt[];
    return events = tasks.map((dt: TaskDto) => {
      let eventCalendar: EventsCalendarInt = {
        id: dt.id,
        title: dt.task,
        start: dt.date,
      };

      return eventCalendar;
    });
  }

}
