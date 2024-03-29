export class Tool {
  public static isEmpty (obj: any) {
    if ((typeof obj === 'string')) {
      return !obj || obj.replace(/\s+/g, "") === ""
    } else {
      return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
    }
  }

  public static isNotEmpty (obj: any) {
    return !this.isEmpty(obj);
  }

  /**
   * Copy obj
   * @param obj
   */
  public static copy (obj: any) {
    if (Tool.isNotEmpty(obj)) {
      return JSON.parse(JSON.stringify(obj));
    }
  }


  public static array2TreeNew (array: any, parentId: number) {
    if (Tool.isEmpty(array)) {
      return [];
    }

    const result= new Map();
    for (let i = 0; i < array.length; i++) {
      const c = array[i];
      if (result.has(c.parent)) {
        result.get(c.parent).push(c);
      } else {
        const list = [];
        list.push(c);
        result.set(c.parent, list);
      }
    }

    const res = result.get(parentId);
    for (let i = 0; i < array.length; i++) {
      const c = array[i];
      if(result.has(c.id)){
        c.children = result.get(c.id);
      }
    }

    return res;
  }
  /**
   * This implementation is not effective since each recursive call iterate an array, taking O(n^2).
   */
  // public static array2Tree (array: any, parentId: number) {
  //   if (Tool.isEmpty(array)) {
  //     return [];
  //   }
  //
  //   const result  = [];
  //   for (let i = 0; i < array.length; i++) {
  //     const c = array[i];
  //     // console.log(Number(c.parent), Number(parentId));
  //     if (Number(c.parent) === Number(parentId)) {
  //       result.push(c);
  //
  //
  //       const children = Tool.array2Tree(array, c.id);
  //       if (Tool.isNotEmpty(children)) {
  //         c.children = children;
  //       }
  //     }
  //   }
  //   return result;
  // }

  /**
   * @param len
   * @param radix Default 62
   * @returns {string}
   */
  public static uuid (len: number, radix = 62) {
    const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    const uuid = [];
    radix = radix || chars.length;

    for (let i = 0; i < len; i++) {
      uuid[i] = chars[0 | Math.random() * radix];
    }

    return uuid.join('');
  }
}
