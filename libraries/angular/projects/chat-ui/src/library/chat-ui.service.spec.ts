import { TestBed } from '@angular/core/testing';

import { ChatUiService } from './chat-ui.service';

describe('ChatUiService', () => {
  let service: ChatUiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatUiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
