export interface Result {
  hasMore: boolean;
  items: ResultItem[];
}

export interface ResultItem {
  title: string;
  author: string;
  creationDate: number;
  answered: boolean;
  link: string;
}
