import { Component, Input } from '@angular/core';
import { ForumMessagesDto } from 'src/app/models/forum/forum-dto/forum-dto';

@Component({
  selector: 'app-entries-forum',
  templateUrl: './entries-forum.component.html',
  styleUrls: ['./entries-forum.component.css']
})
export class EntriesForumComponent {
  @Input()
  public entry!: ForumMessagesDto;

  constructor(){}

}
