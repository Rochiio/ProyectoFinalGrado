import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { ForumMessagesDto } from 'src/app/models/forum/forum-dto/forum-dto';
import { UserToken } from 'src/app/models/user/user-token/user-token';

@Component({
  selector: 'app-entries-forum',
  templateUrl: './entries-forum.component.html',
  styleUrls: ['./entries-forum.component.css']
})
export class EntriesForumComponent implements OnInit{
  @Input()
  public entry!: ForumMessagesDto;
  @Output()
  public messageIdToDelete = new EventEmitter<string>();

  public isAdmin: boolean = false;

  constructor(){}

  ngOnInit(): void {
    this.entry.created_At = this.entry.created_At+".";
    if(localStorage.getItem('isAssociation')! == 'false'){
      let user: UserToken = JSON.parse(localStorage.getItem('currentUser')!);
      if(user.user.rol == 'ADMIN'){
        this.isAdmin = true;
      }
    }
  }

  public deleteEntry(){
    this.messageIdToDelete.emit(this.entry.id);
  }

}
