import { TagModel } from './tagModel';

describe('Tag', () => {
  it('should create an instance', () => {
    expect(new TagModel('test')).toBeTruthy();
  });
});
