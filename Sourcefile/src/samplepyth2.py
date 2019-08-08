# Python3 code to demonstrate
# Dictionary creation using list contents
# using Dictionary comprehension + zip()

# initializing list
keys_list = ["key1", "key2"]
nested_name = ["Manjeet", "Nikhil"]
nested_age = [22, 21]

# printing original lists
print("The original key list : " + str(keys_list))
print("The original nested name list : " + str(nested_name))
print("The original nested age list : " + str(nested_age))

# using Dictionary comprehension + zip()
# Dictionary creation using list contents
res = {key: {'name': name, 'age': age} for key, name, age in
    zip(keys_list, nested_name, nested_age)}

# print result
print("The dictionary after construction : " + str(res)) 
