def main():
   # declaring and initializing some variables
   x = 5
   y = 'hello'
   z = 9.8
    
   # printing the variables
   print('x:', x, 'y:', y, 'z:', z)
   
   # a list (make an array in java)
   nums = [3, 6, -1, 2]
   for num in nums:
      print(num)
      
   # call a function
   numFound = char_count(y, 'l')   
   print("Found:", numFound)
   
   # a counting for loop
   for i in range(1,11):
      print(i, end=' ')

   print()
   
def char_count(s, c):
   count = 0
   for ch in s:
      if ch == c:
         count += 1
   return count

if __name__ == "__main__":
   main()
