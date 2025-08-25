import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatUi } from './chat-ui.component';

describe('ChatUiComponent', () => {
  let component: ChatUi;
  let fixture: ComponentFixture<ChatUi>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatUi]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatUi);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
